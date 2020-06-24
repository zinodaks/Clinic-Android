<?php
if($_POST) { 
    require_once("connect.php");
    $username = $_POST['username'];
    $symptoms = $_POST['symptoms'];
    $date = explode("/" , $_POST['date']);
    $date = $date[0]."-".$date[1]."-".$date[2];
    $response = array();

    $query = "select * from appointments where patientUsername='$username' AND date='$date' ;";
    if ($result = $link->query($query)) { 
        if($result->num_rows > 0 ) { 
            $response['error'] = true ;
            echo json_encode($response);
        } else { 
            $query = "insert into appointments values('$username' , '$date' , '$symptoms');";
            if($link->query($query)) { 
                $response['error'] = false ;
                echo json_encode($response);
            }
        }
    }
}





?>