<?php 
if($_POST) { 
    require_once("connect.php");
    $username = $_POST['username'];
    $response = array();
    
    $query = "select * from consultations where patientUsername='$username';";
    if($result = $link->query($query)) { 
        if($result->num_rows > 0) {
            $index = 0 ;
            $consultations = array();
        while($row = $result->fetch_assoc()) {
            $consultations[$index] = array(
                'consultation' => array(
                $row['date'], 
                $row['medicine'],
                $row['instructions']
                )
            );
            $index++;
        }
        $response['error'] = false ;
        $response['consultations'] = $consultations;
        echo json_encode($response);
    }
    }
}
?>