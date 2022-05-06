<?php
	
	include "connection.php";
	
	$id = isset($_POST['id']) ? $_POST['id'] : '';
	$name = isset($_POST['name']) ? $_POST['name'] : '';
	$hobby = isset($_POST['hobby']) ? $_POST['hobby'] : '';
	$password = isset($_POST['password']) ? $_POST['password'] : '';
	
	
	if (empty($hobby) || empty($name) || empty($password)) { 
		echo "Fields cannot be empty"; 
		
	} else  { 

		if(empty($id)) {
		$query = mysqli_query($con,"INSERT INTO mahasiswa (id,name, hobby,password) VALUES(0,'".$name."','".$hobby."','".$password."')");
		
		if ($query) {
			echo "Insert successfully";
			
		} else{ 
			echo "Insert Failed";
			
		}
	}else{
		$query = mysqli_query($con,"UPDATE mahasiswa set name = '".$name."', hobby = '".$hobby."', password = '".$password."' where id = '". $id ."'");
		
		if ($query) {
			echo "Edit sucessfully";
			
		} else{ 
			echo "Edit Failed";
			
		}
		}
	}
?>