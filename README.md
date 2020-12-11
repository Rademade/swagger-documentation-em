# Documentation Task
## Input
### Task
#### Description
**AS** a third party application developer

**I WANT** to have interactive swagger documentation

**SO THAT** I can easily create endpoints using API

There is a list of the most valuable API endpoints:
* `POST /api/v1/endpoint` - endpoint creation.
* `POST /api/v1/service_profile` - create service profile.

_Be aware that there are some gaps left in the provided files so that you will need to contact us in order to fulfill them._

#### Suggested approach
- Learn structure of applications
- Learn about swagger
- Import `openapi.yml` into swagger editor(or any other editor)
- Learn what of the required API endpoints are present already
- Clarify requirements
- To plan sequence of the task execution
- Find all the information required to complete those API Endpoints documentation
- Update the `openapi.yml`
- Test that it works using provided api keys
- Schedule and host a demo
#### How to demo
- Show the process of `service_profile` and `endpoint` using created API Documentation in interactive mode
- Make a pull-request with updated `openapi.yml`
#### When it is acceptable result?
- Generated documentation is valid
- You assumed that your vision of resulting documentation is correct and assumptions were lucky
- It's possible to complete sequence create `service_profile` -> create an `endpoint`
#### When it is a good result?
- A response schema provided according to the [specification](https://swagger.io/docs/specification/describing-responses/)
and the responce schema is inline with what endpoint returns
- You have gotten to the result using communication
- Serialized objects(request/response) are not inlined but extracted as [DataModels](https://swagger.io/docs/specification/data-models/)
  and correctly referenced
#### When it is an excellent result?
- You built SDK and provided sample app that creates 

### Structure parts
* /java - Java API Application
* /php - PHP API Application
* routes - The mapping between API paths and corresponding controllers in both applications
* openapi.yml - Existing API documentation file, that needs to be updated, extended

## Resources
* Swagger specification documentation - https://swagger.io/docs/specification/about/
* Swagger editor - https://editor.swagger.io/
