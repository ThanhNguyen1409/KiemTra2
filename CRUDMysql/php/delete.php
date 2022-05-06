<?php
	
	include "connection.php";
	
	$id = isset($_POST['id']) ? $_POST['id'] : '';
	

	if (empty($id)) { 
		echo "id can not be empty"; 
		
	} else  {
		$query = mysqli_query($con,"DELETE FROM MAHASISWA WHERE id = '".$id."'");
		
		if ($query) {
			echo "Delete sucessfully";
			
		} else{ 
			echo "Delete Failed";
			
		}	
	}
?>