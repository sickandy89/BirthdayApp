<?php

/*
 * Following code will list all the products
 */

// array for JSON response
$response = array ();
if (isset ( $_POST ['id'] ) && isset($_POST['messageTab'])) {
	$id = $_POST['id'] ;
	$messageTab = $_POST['messageTab'];
	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
	
	// connecting to db
	$db = new DB_CONNECT ();
	
	// get all products from products table
	$result = mysql_query ( "SELECT * FROM .$messageTab where id = '$id' " ) or die ( mysql_error () );
	
	// check for empty result
	if (mysql_num_rows ( $result ) > 0) {
		// looping through all results
		// products node
		$response ["messages"] = array ();
		
		while ( $row = mysql_fetch_array ( $result ) ) {
			// temp user array
			$message = array ();
			$product ["id"] = $row ["id"];
			$product ["message"] = $row ["message"];
			
			// push single product into final response array
			array_push ( $response ["messages"], $product );
		}
		// success
		$response ["success"] = 1;
		
		// echoing JSON response
		echo json_encode ( $response );
	} else {
		// no products found
		$response ["success"] = 0;
		$response ["message"] = "No messages found";
		
		// echo no users JSON
		echo json_encode ( $response );
	}
}
?>