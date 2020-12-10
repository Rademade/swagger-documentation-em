package json;

public abstract class EndpointJson {
  public static class EndpointSerializer extends JsonSerializer<Endpoint> {

    @Override
    public void serialize(Endpoint value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeStartObject();
      jgen.writeNumberField("id", value.getId());
      provider.defaultSerializeField("created", value.getCreated(), jgen);
      serialize(value.getServiceProfile(), jgen);
      serialize(value.getSims(), jgen, provider);

      jgen.writeStringField("ip_address", value.getIpAddress());
      serialize(value.getIpAddressSpace(), jgen);
      jgen.writeEndObject();
    }

    @Override
    public Class<Endpoint> handledType() {
      return Endpoint.class;
    }

    private void serialize(ServiceProfile value, JsonGenerator jgen) throws IOException {
      if (value != null) {
        jgen.writeFieldName("service_profile");
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("name", value.getName());
        jgen.writeEndObject();
      }
    }

    // only one Sim per Endpoint is supported
    private void serialize(List<Sim> value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException {
      if (!value.isEmpty()) {
        Sim sim = value.get(0);

        jgen.writeFieldName("sim");
        jgen.writeStartObject();
        jgen.writeNumberField("id", sim.getId());
        jgen.writeEndObject();
      }
    }

  public static class EndpointDeserializer extends JsonDeserializer<Endpoint> {

    @Override
    public Endpoint deserialize(JsonParser jsonParser, DeserializationContext dcon)
        throws IOException {

      Endpoint endpoint = new Endpoint();

      ObjectDeserializer od = new ObjectDeserializer("Endpoint");
      od.init(jsonParser, (root) -> {
        if (!root.hasNonNull("sim")) {
          endpoint.setSims(null); // do not update sims in spc-backend
        }
      });

      od.deserialize((name, node) -> {
        switch (name) {
          case "service_profile": {
            od.deserialize(ServiceProfile.class, node, v -> setServiceProfile(endpoint, v, od));
          }
            break;

          case "sim": {
            od.deserialize(Sim.class, node, v -> addSim(endpoint, v, od));
          }
            break;
          case "ip_address": {
            if (node.isNull()) {
              od.error(new InvalidValueError("Endpoint", "ip_address", "null"));
            } else {
              endpoint.setIpAddress(node.textValue());
            }
          }

          default: {
            od.unknownProperty(name);
          }
        }
      });

      return endpoint;
    }

    @Override
    public Class<Endpoint> handledType() {
      return Endpoint.class;
    }

    private void setServiceProfile(Endpoint endpoint, ServiceProfile value, ObjectDeserializer od) {
      if (value != null && value.getId() != null) {
        endpoint.setServiceProfile(value);
      } else {
        od.error(new InvalidServiceProfileIdError(""));
      }
    }

    private void addSim(Endpoint endpoint, Sim value, ObjectDeserializer od) {
      if (value != null && value.getId() != null) {
        endpoint.getSims().add(value);
      }
    }
  }

  public static void register(SimpleModule module) {
    module.addSerializer(new EndpointSerializer());
    module.addDeserializer(Endpoint.class, new EndpointDeserializer());
  }
}
