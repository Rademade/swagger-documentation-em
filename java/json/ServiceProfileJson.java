package json;

public abstract class ServiceProfileJson {
  public static class ServiceProfileSerializer extends JsonSerializer<ServiceProfile> {

    @Override
    public void serialize(ServiceProfile value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeStartObject();

      jgen.writeNumberField("id", value.getId());
      if (value.getDescription() != null) {
        jgen.writeStringField("description", value.getDescription());
      }
      jgen.writeBooleanField("allowed_3g", value.getAllowed3g());
      jgen.writeBooleanField("allowed_4g", value.getAllowed4g());
      jgen.writeEndObject();
    }

    @Override
    public Class<ServiceProfile> handledType() {
      return ServiceProfile.class;
    }
  }

  public static class ServiceProfileDeserializer extends JsonDeserializer<ServiceProfile> {

    @Override
    public ServiceProfile deserialize(JsonParser jsonParser, DeserializationContext dcon)
        throws IOException {

      ServiceProfile profile = new ServiceProfile();

      ObjectDeserializer od = new ObjectDeserializer("ServiceProfile");

      applyDataQuotaExists = false;
      od.deserialize(jsonParser, (name, node) -> {
        switch (name) {
          case "id": {
            profile.setId(JsonSupport.toLong(node, InvalidServiceProfileIdError::new));
          }
            break;
          case "description": {
            profile.setDescription(node.textValue());
          }
            break;
          case "allowed_3g": {
            if (node != null && node.isBoolean()) {
              profile.setAllowed3g(node.booleanValue());
            } else if (node != null && node.isInt()) {
              profile.setAllowed3g(node.intValue() != 0);
            } else {
              od.error(new InvalidFormatBooleanError("Service Profile", name));
            }
          }
          break;
          case "allowed_4g": {
            if (node != null && node.isBoolean()) {
              profile.setAllowed4g(node.booleanValue());
            } else if (node != null && node.isInt()) {
              profile.setAllowed4g(node.intValue() != 0);
            } else {
              od.error(new InvalidFormatBooleanError("Service Profile", name));
            }
          }
          break;
          default: {
            od.unknownProperty(name);
          }
        }
      });

      return profile;
    }

    @Override
    public Class<ServiceProfile> handledType() {
      return ServiceProfile.class;
    }
  }

  public static void register(SimpleModule module) {
    module.addSerializer(new ServiceProfileSerializer());
    module.addDeserializer(ServiceProfile.class, new ServiceProfileDeserializer());
  }
}
