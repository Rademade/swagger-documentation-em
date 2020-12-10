package data;

public class Sim implements KryoSerializable {
  private static final long serialVersionUID = 1L;

  private static Map<String, String> sortFields;
  static {
    Map<String, String> tmpMap = new HashMap<String, String>();
    tmpMap.put("id", "id");
    tmpMap.put("issuer_org", "issuerOrg");
    tmpMap.put("customer_org", "customerOrg");
    tmpMap.put("endpoint", "endpoint");
    sortFields = Collections.unmodifiableMap(tmpMap);
  }

  private Long id;
  private Organisation issuerOrg;
  private Organisation customerOrg;
  private Endpoint endpoint;

  // runtime data
  private Location location;
  private MessageWaitingData messageWaitingData;

  public Sim() {
  }

  public Sim(Long id, Organisation issuerOrg, Organisation customerOrg,
      Endpoint endpoint) {
    this.id = id;
    this.issuerOrg = issuerOrg;
    this.customerOrg = customerOrg;
    this.endpoint = endpoint;
  }
}
