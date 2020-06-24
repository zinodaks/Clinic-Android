<?php 
if($_POST) { 
require_once("connect.php");
$username = $_POST['username'];
$password = $_POST['password'];
$response = array();


$query = "select * from managers where username='$username';";
if($result = $link->query($query)) { 
    if($result->num_rows > 0) { 
        $row = $result->fetch_assoc();
        if($row['password'] == $password) {
            $user = array(
                'username' => $row['username']
            );
            $response['success'] = true ;
            $response['manager'] = true ;
            $response['user'] = $user; 
            echo json_encode($response);
        } else {
            $response['success'] = false ;
            echo json_encode($response);
        }
    } else {
        $query = "select * from accounts where username = '$username';";
if($result = $link->query($query)) {
    if($result->num_rows > 0 ) {
        $row = $result->fetch_assoc();
        if($row['password'] == $password) {
            $query = "select * from patientinfo where username='$username';";
            $result = $link->query($query);
            $row = $result->fetch_assoc();
            $user = array(
                'username' => $row['username'] , 
                'f_name' => $row['firstName'] , 
                'l_name' => $row['lastName'] ,
                'dob' => $row['DOB']
            );
            $response['manager'] = false;
            $response['success'] = true;
            $response['user'] = $user ;
            echo json_encode($response);
          } else {
            $response['success'] = false ;
            echo json_encode($response);
           }
    } else {
        $response['success'] = false ;
        echo json_encode($response);
    }
}
    }
}
}
?>