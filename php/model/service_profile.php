<?PHP

class ServiceProfile extends AppModel
{
    public $name = 'ServiceProfile';
    public $useTable = 'service_profile';
    public $primaryKey = 'service_profile_id';
    public $cacheQueries = true;
    var $virtualFields = array(
        'used_count' => "(SELECT count(*) FROM endpoint WHERE service_profile_id = ServiceProfile.service_profile_id)"
    );

    public function beforeFind($queryData)
    {
        $filter = "...";

        if (is_array($queryData['conditions'])) {
            array_push($queryData['conditions'], $filter);
        } else {
            $queryData['conditions'] = $filter;
        }

        $queryData['limit'] = 2000;

        return $queryData;
    }

    public function beforeDelete($cascade = true)
    {
        $serviceProfile = $this->findByServiceProfileId($this->id);

        if ($serviceProfile['ServiceProfile']['organisation_id'] == $this->token_data['esc.org']) {
            return true;
        }

        return false;
    }
}

?>
