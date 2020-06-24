<?php
if($_POST) { 
    require_once("connect.php");
    $response = array();
    $username = $_POST['username'];
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $dateOfBirth = $_POST['dateOfBirth'];
    $dateOfBirth = explode("/" , $dateOfBirth);
    $dateOfBirth= "$dateOfBirth[0]"."-"."$dateOfBirth[1]"."-"."$dateOfBirth[2]";
    $password = $_POST['password'];
    $ssn = $_POST['ssn'];

    $query = "select * from accounts where username='$username';";
    if ($result = $link->query($query)) {
        if ($result->num_rows > 0) {
            $response['error'] = true;
        }
    } 

    if(!isset($response['error'])) {
        $query = "insert into accounts values('$username' , '$password');";
        $link->query($query);
        $query = "insert into patientinfo values('$username' , '$fname' , '$lname' ,  '$ssn' , '$dateOfBirth');";
        $link->query($query);
        $response['error'] = false;
        $user = array(
            'username' => $username,
            'f_name' => $fname,
            'l_name' => $lname,
            'dob' => $dateOfBirth 
        );
        $response['user'] = $user;
    }
    echo json_encode($response);
}

?>