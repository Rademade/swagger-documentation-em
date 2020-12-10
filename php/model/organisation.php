
<?PHP

class Organisation extends AppModel
{
  public $name = 'Organisation';
  public $useTable = 'organisation';
  public $primaryKey = 'organisation_id';
  public $cacheQueries = true;

  public function beforeFind($queryData)
  {
      $filter = " ...";

      if (is_array($queryData['conditions'])) {
          //  array_push($queryData['conditions'], $filter);
      } else {
          $queryData['conditions'] = $filter;
      }

      $queryData['order'] = "Organisation.organisation_id";
      return $queryData;
  }
}

?>
