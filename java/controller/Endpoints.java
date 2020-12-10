package controllers;

/**
 * Controller for Endpoint API calls
 *
 */
@Singleton
public class Endpoints extends AuthenticatedController {

  private final ActorSelection nsProxy;
  private final long masterOrganisationId;
  private final long backendTimeout;
  private final long nsproxyTimeout;

  static int USSD_GSM7_MAX_LENGTH = 164;
  static int USSD_UCS2_MAX_LENGTH = 72;

  /**
   * @param system ActorSystem
   * @param config Configuration
   */
  @Inject
  public Endpoints(ActorSystem system, Configuration config) {
    super(system, config);
    nsProxy = system.actorSelection("user/nsproxy");
    masterOrganisationId = config.getLong("organisation.id");
    backendTimeout = config.getMilliseconds("backend.timeout");
    nsproxyTimeout = config.getMilliseconds("nsproxy.timeout");

    preparePagingSettings(config, "endpoint", 100, 200);
  }

  /**
   * @param id of Endpoint to get
   * @return Promise of Result
   */
  @BodyParser.Of(BodyParser.Empty.class)
  public CompletionStage<Result> byId(Long id) {
    return query(new QueryById(null, id), null, null);
  }

  /**
   * @param q String
   * @param page Integer
   * @param per_page Integer
   * @param sort String
   * @return Promise of Result
   */
  @BodyParser.Of(BodyParser.Empty.class)
  public CompletionStage<Result> list(String q, Integer page, Integer per_page, String sort) {

    PagingInfo pagingInfo;
    try {
      pagingInfo = handlePagingRequest(q, page, per_page, sort, Endpoint.getSortFields(), null);
    } catch (PagingError e) {
      return CompletableFuture.completedFuture(spcError(e));
    }
    return query(new QueryByOrganisation(null, getOrganisationId().toString(), pagingInfo), q, sort);
  }


  /**
   * @return Promise of Result
   */
  @BodyParser.Of(BodyParser.Json.class)
  public CompletionStage<Result> create() {
    JsonNode json = request().body().asJson();

    if (json == null) {
      return CompletableFuture
          .completedFuture(spcError(new BadRequestError("Expecting Json data")));
    }
    Endpoint ep;
    try {
      ep = Json.fromJson(json, Endpoint.class);
      ep = validate(ep);
    } catch (SpcError e) {
      return CompletableFuture.completedFuture(spcError(e));
    }
    CreateEndpoint request = new CreateEndpoint(ep);
    request.getEndpoint().setOrganisation(getOrganisation());

    return askBackend(
        request,
        CreateEndpointResult.class,
        (response) -> {
          if (response.isError()) {
            return spcError(response.getError());
          }

          response().setHeader(
              Http.HeaderNames.LOCATION,
              String.format("%s/%d", controllers.routes.Endpoints.create().absoluteURL(request()), response
                  .getEndpoint().getId()));

          return created();
        });
  }

  /**
   * @param id of Endpoint to delete
   * @return Promise of Result
   */
  @BodyParser.Of(BodyParser.Empty.class)
  public CompletionStage<Result> delete(Long id) {
    Endpoint ep = new Endpoint();
    ep.setId(id);
    ep.setOrganisation(getOrganisation());
    DeleteEndpoint request = new DeleteEndpoint(ep);

    return askBackend(request, DeleteEndpointResult.class, (response) -> {
      if (response.isError()) {
        return spcError(response.getError());
      }

      return noContent();
    });
  }

  /**
   * @param id of Endpoint to update
   * @return Promise of Result
   */
  @BodyParser.Of(BodyParser.Json.class)
  public CompletionStage<Result> update(Long id) {
    JsonNode json = request().body().asJson();

    if (json == null) {
      return CompletableFuture
          .completedFuture(spcError(new BadRequestError("Expecting Json data")));
    }
    Endpoint endpoint;
    try {
      endpoint = Json.fromJson(json, Endpoint.class);
      endpoint = validate(endpoint);
    } catch (SpcError e) {
      return CompletableFuture.completedFuture(spcError(e));
    }
    endpoint.setId(id);
    endpoint.setOrganisation(getOrganisation());
    UpdateEndpoint request = new UpdateEndpoint(endpoint);
    return askBackend(request, UpdateEndpointResult.class, (response) -> {
      if (response.isError()) {
        return spcError(response.getError());
      }

      return noContent();
    });
  }

}
