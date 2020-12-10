package data;

public class Organisation implements KryoSerializable {
  private static final long serialVersionUID = 3L;

  private static Map<String, String> sortFields;

  static {
    Map<String, String> tmpMap = new HashMap<>();
    tmpMap.put("id", "id");
    tmpMap.put("name", "name");
    sortFields = Collections.unmodifiableMap(tmpMap);
  }

  private Long id;
  private String name;

  public Organisation() {}

  public Organisation(Long id, String name, ) {
    this.id = id;
    this.name = name;
  }

  public Organisation(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
