package json;

public abstract class OrganisationJson {

  public static class OrganisationSerializer extends JsonSerializer<Organisation> {

    @Override
    public void serialize(Organisation value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeStartObject();
      jgen.writeNumberField("id", value.getId());
      jgen.writeStringField("name", value.getName());
      jgen.writeEndObject();
    }

    @Override
    public Class<Organisation> handledType() {
      return Organisation.class;
    }
  }

  public static class OrganisationDeserializer extends JsonDeserializer<Organisation> {

    @Override
    public Organisation deserialize(JsonParser jsonParser, DeserializationContext dcon)
        throws IOException {

      Organisation organisation = new Organisation();

      ObjectDeserializer od = new ObjectDeserializer("Organisation");
      od.init(jsonParser);

      od.deserialize((name, node) -> {
        switch (name) {
          case "id": {
            organisation.setId(JsonSupport.toNullableLong(node, InvalidOrganisationIdError::new));
          }
            break;

          case "name": {
            organisation.setName(node.textValue());
          }
            break;

          default: {
            od.unknownProperty(name);
          }
        }
      });

      return organisation;
    }

    @Override
    public Class<Organisation> handledType() {
      return Organisation.class;
    }
  }

  public static void register(SimpleModule module) {
    module.addSerializer(new OrganisationSerializer());
    module.addDeserializer(Organisation.class, new OrganisationDeserializer());
  }
}
