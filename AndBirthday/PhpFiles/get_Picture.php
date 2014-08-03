<?php

/*
 * Following code will list all the products
 */

// array for JSON response
$response = array ();

	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
	
	// connecting to db
	$db = new DB_CONNECT ();		
	
	$sql = "SELECT picture FROM Pictures WHERE id=0";
	$result = mysql_query("$sql");
	header("Content-type: image/jpeg");
	echo mysql_result($result, 0);
	mysql_close($link);
	echo 'test';

?>