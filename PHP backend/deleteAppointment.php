<?php 
if($_POST) { 

    require_once("connect.php");
    $username = $_POST['username'];
    $date = $_POST['date'];
    $response = array();

    $query = "delete from appointments where patientUsername='$username' AND date='$date';";
    if($result = $link->query($query)){
        $response['error'] = false ;
        echo json_encode($response);
    }
}



?>