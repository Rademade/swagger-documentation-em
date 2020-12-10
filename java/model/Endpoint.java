package data;

public class Endpoint implements KryoSerializable {
  private static final long serialVersionUID = 3L;
  private static Map<String, String> sortFields;
  static {
    Map<String, String> tmpMap = new HashMap<String, String>();
    tmpMap.put("id", "id");
    tmpMap.put("service_profile", "serviceProfile");
    sortFields = Collections.unmodifiableMap(tmpMap);
  }

  private Long id;
  private String ipAddress;
  private BigDecimal ipAddressNumber;
  private Date created;
  private List<EndpointBlockedInfo> endpointBlockedInfo;
  private ServiceProfile serviceProfile;
  private Organisation organisation;
  private List<Sim> sims = new ArrayList<>();

  public Endpoint() {}

  public Endpoint(Long id, String ipAddress, Date created, ServiceProfile serviceProfile, Organisation organisation,
      List<Sim> sims, List<EndpointBlockedInfo> endpointBlockedInfo) {
    this.id = id;
    this.ipAddress = ipAddress;
    this.created = created;
    this.serviceProfile = serviceProfile;
    this.organisation = organisation;
    this.sims = sims;
    this.endpointBlockedInfo = endpointBlockedInfo;
  }
}
