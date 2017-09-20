<?php
class Product{

    // database connection and table name
    private $conn;
    //private $table_name = "products";
    private $table_name = "info";

    // object properties
    // public $id;
    // public $name;
    // public $description;
    // public $price;
    // public $category_id;
    // public $category_name;
    // public $created;

    public $ModuleNumber;
    public $username;
    public $ErrorType;
    public $ErrorMessage;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read products
function read(){

    // select all query from product table (api_db)
    // $query = "SELECT
    //             c.name as category_name, p.id, p.name, p.description, p.price, p.category_id, p.created
    //         FROM
    //             " . $this->table_name . " p
    //             LEFT JOIN
    //                 categories c
    //                     ON p.category_id = c.id
    //         ORDER BY
    //             p.created DESC";

    // select all quesry fromm info table (robin_db database)
    //$query = "SELECT * FROM info"; //. $this->table_name;
    $query = "SELECT * FROM
                  " . $this->table_name;

    // prepare query statement
    $stmt = $this->conn->prepare($query);

    // execute query
    $stmt->execute();

    return $stmt;
}

// create product
function create(){

    // query to insert record
    $query = "INSERT INTO
                " . $this->table_name . "
            SET
                ModuleNumber=:mn, username=:username, ErrorType=:ErrorType, ErrorMessage=:ErrorMessage";

                //database_field_name:=variable_name
    // prepare query
    $stmt = $this->conn->prepare($query);

    // sanitize
    $this->module=htmlspecialchars(strip_tags($this->ModuleNumber));
    $this->username=htmlspecialchars(strip_tags($this->username));
    $this->type=htmlspecialchars(strip_tags($this->ErrorType));
    $this->msg=htmlspecialchars(strip_tags($this->ErrorMessage));

    // bind values
    $stmt->bindParam(":mn", $this->module);
    $stmt->bindParam(":username", $this->username);
    $stmt->bindParam(":ErrorType", $this->type);
    $stmt->bindParam(":ErrorMessage", $this->msg);

    // execute query
    if($stmt->execute()){
        return true;
    }else{
        return false;
    }
}
}
