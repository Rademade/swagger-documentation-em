package data;

@SuppressWarnings("javadoc")
public class ServiceProfile implements KryoSerializable {
  private static final long serialVersionUID = 4L;

  private Long id;
  private String description;
  private Organisation organisation;
  private Boolean allowed3g;
  private Boolean allowed4g;

  private List<Service> services = new ArrayList<>();

  public ServiceProfile() {}

  public ServiceProfile(Long id, String description, Organisation organisation,
      Boolean allowed3g, Boolean allowed4g) {
    this.id = id
    this.description = description;
    this.organisation = organisation;
    this.allowed3g = allowed3g;
    this.allowed4g = allowed4g;
  }
}
