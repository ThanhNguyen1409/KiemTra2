<?php
	include "connection.php";
	
	$id = isset($_POST['id']) ? $_POST['id'] : '';

	class emp{}
	
	if (empty($id)) { 
		echo "id can not be empty"; 
		
	} else  
		
	 {
		$query 	= mysqli_query($con,"SELECT * FROM mahasiswa WHERE id='".$id."'");
		$row 	= mysqli_fetch_array($query);
		
		if (!empty($row)) {
			$response = new emp();
			$response->id = $row["id"];
			$response->name = $row["name"];
			$response->hobby = $row["hobby"];
			$response->password = $row["password"];
			
			echo(json_encode($response));
		} else{ 
			
			echo("Error Retrieving Data"); 
		}	
	}
?>