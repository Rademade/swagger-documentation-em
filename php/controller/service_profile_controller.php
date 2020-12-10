<?php

class ServiceProfileController extends AppController
{
    public $components = array('RequestHandler');
    public $name = 'ServiceProfile';
    public $uses = array(
        'ServiceProfile',
        'Endpoint');
    public $mutableFields = array(
        "allowed_3g",
        "allowed_4g"
    );

    public function view($id, $resetSmppPassword = false)
    {
        $data = $this->ServiceProfile->get($id);

        if ($data) {
            $data = $this->expand($data, $this->json_expands);
            $data['allowed_3g'] = IntVal($data['allowed_3g']);
            $data['allowed_4g'] = IntVal($data['allowed_4g']);
            $this->return_json($data);
        } else {
            $this->header('HTTP/1.1 404 Not found');
            $this->return_empty_body();
        }
    }

    public function add()
    {
        $this->create_or_update(null);
    }

    public function update($id)
    {
        $serviceProfile = $this->ServiceProfile->findByServiceProfileId($id);

        if ($serviceProfile['ServiceProfile']['organisation_id'] != $this->token_data['esc.org']) {
            $this->header('HTTP/1.1 403 Forbidden');
            $this->return_json_error('This Service Profile does not belong to your organisation.');
        } else {
            $this->create_or_update($id);
        }
    }

    public function create_or_update($id)
    {
        if ($id == null) {
            $this->ServiceProfile->create();
            $this->ServiceProfile->set('organisation_id', $this->token_data['esc.org']);
        } else {
            $this->ServiceProfile->id = $id;
        }

        $this->setMutableFields();

        if ($this->ServiceProfile->save()) {
            $this->request_data['service_profile_id'] = $this->ServiceProfile->id;

            $this->header('HTTP/1.1 201 Created');

            $this->return_json($this->request_data);
        } else {
            $this->header('HTTP/1.1 422 Unprocessable Entity');
            $this->return_json_error('Error adding the Service Profile to the database.');
        }
    }

    public function delete($id)
    {
        if (!$this->ServiceProfile->get($id)) {
            $this->header('HTTP/1.1 404 Not Found');
            $this->return_json_error('service_profile not found');
        }
        $db = $this->ServiceProfile->getDataSource();
        $db->begin($this->ServiceProfile);

        if (
            $this->ServiceProfile->delete($id)
        ) {
            $db->commit($this->ServiceProfile);
            $this->return_empty_body();
        } else {
            $db->rollback($this->ServiceProfile);
            $this->header('HTTP/1.1 422 Unprocessable Entity');
            $this->return_json_error('Service Profile still used by endpoints, please update your endpoints to use another Service Profile.');
        }
    }
}
?>
