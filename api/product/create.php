<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once '../config/database.php';

// instantiate product object
include_once '../objects/product.php';

$database = new Database();
$db = $database->getConnection();

$product = new Product($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

// set product property values
//$product->database_field_name = $data->posted_json_field_name
$product->ModuleNumber = $data->module;
$product->username = $data->username;
$product->ErrorType = $data->type;
$product->ErrorMessage = $data->msg;


// create the product
if(empty($product->ModuleNumber) && empty($product->username) && empty($product->ErrorType) && empty($product->ErrorMessage)){
  echo '{';
      echo '"message": "no data to be inserted"';
  echo '}';
}
else{
  if($product->create()){
      echo '{';
          echo '"message": "Product was created."';
      echo '}';
  }
  // if unable to create the product, tell the user
  else{
      echo '{';
          echo '"message": "Unable to create product."';
      echo '}';
  }
}



?>
