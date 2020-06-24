<?php 
if($_POST) {
    require_once("connect.php");
    $username = $_POST['username'];
    $date = $_POST['date'];
    $medicine = $_POST['medicine'];
    $instructions = $_POST['instructions'];
    $response = array();

    $query = "insert into consultations values('$username' , '$date' , '$medicine' , '$instructions');";
    if($result = $link->query($query)) {
        $response['error'] = false;
        echo json_encode($response);
    } else {
        $response['error'] = true;
        echo json_encode($response);
    }




}


?>