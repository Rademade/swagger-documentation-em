package json;

public abstract class SimJson {

  private static final Logger log = LoggerFactory.getLogger(SimJson.class);

  public static class SimSerializer extends JsonSerializer<Sim> {

    @Override
    public void serialize(Sim value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeStartObject();
      jgen.writeNumberField("id", value.getId());
      serialize("customer_org", value.getCustomerOrg(), jgen, provider);
      serialize("reseller_org", value.getResellerOrg(), jgen, provider);
      serialize(value.getEndpoint(), jgen, provider);
      jgen.writeEndObject();
    }

    @Override
    public Class<Sim> handledType() {
      return Sim.class;
    }

    private void serialize(String fieldName, Organisation value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException {
      if (value != null) {
        jgen.writeFieldName(fieldName);
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("name", value.getName());
        jgen.writeEndObject();
      }
    }

    private void serialize(Endpoint value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException {
      if (value != null) {
        jgen.writeFieldName("endpoint");
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        provider.defaultSerializeField("created", value.getCreated(), jgen);
        jgen.writeNumberField("organisation_id", value.getOrganisation().getId());
        if (value.getServiceProfile() != null) {
          jgen.writeNumberField("service_profile_id", value.getServiceProfile().getId());
        } else {
          log.warn("Incorrectly provisioned SIM/endpoint. Only deleted endpoints may have no "
              + "Service Profile. Endpoint id: " + value.getId());
        }
        jgen.writeStringField("ip_address", value.getIpAddress());
        jgen.writeEndObject();
      }
    }
  }

  public static class SimDeserializer extends JsonDeserializer<Sim> {

    @Override
    public Sim deserialize(JsonParser jsonParser, DeserializationContext dcon) throws IOException {

      Sim sim = new Sim();

      ObjectDeserializer od = new ObjectDeserializer("Sim");

      od.deserialize(jsonParser, (name, node) -> {
        switch (name) {
          case "id": {
            sim.setId(JsonSupport.toNullableLong(node, InvalidSimIdError::new));
          }
            break;

          case "customer_org": {
            od.deserialize(Organisation.class, node, sim::setCustomerOrg);
          }
            break;

          case "issuer_org": {
            od.deserialize(Organisation.class, node, sim::setIssuerOrg);
          }
            break;

          default: {
            od.unknownProperty(name);
          }
        }
      });

      return sim;
    }

    @Override
    public Class<Sim> handledType() {
      return Sim.class;
    }
  }

  public static void register(SimpleModule module) {
    module.addSerializer(new SimSerializer());
    module.addDeserializer(Sim.class, new SimDeserializer());
  }
}
