public class EndpointBlockedInfo implements KryoSerializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  private String description;
  private Endpoint endpoint;

  public EndpointBlockedInfo() {}

  public EndpointBlockedInfo(Long id, String description, Endpoint endpoint) {
    this.setId(id);
    this.setDescription(description);
    this.setEndpoint(endpoint);
  }
}
