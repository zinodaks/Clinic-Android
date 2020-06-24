<?php 
if($_POST) { 
    require_once("connect.php");
    $username = $_POST['username'];
    $response = array();
    

    $query = "select * from managers where username='$username';";
    if($result = $link->query($query)) {
        $id = 0 ;
        $appointments = array();
        if($result->num_rows > 0 ) {
            $query = "select * from appointments ;";
            if($result = $link->query($query)) { 
                while($row = $result->fetch_assoc()){
                $appointments[$id] = array(
                    'appointment' => array(
                    $row['patientUsername'],
                    $row['date'],
                    $row['symptomsDescription']
                )
                );
                $id++;
            }
                $response['error'] = false ;
                $response['appointments'] = $appointments;
                echo json_encode($response);
            }
        } else {
            $query = "select * from appointments where patientUsername='$username';";
            if($result = $link->query($query)) { 
                $id = 0 ;
                $dates= array();
                $symptoms = array();
                while($row = $result->fetch_assoc()) { 
                    $dates[$id] = $row['date'];
                    $symptoms[$id] = $row['symptomsDescription'];
                    $id++;
                }
                $response['error'] = false ;
                $response['dates'] = $dates ;
                $response['symptoms'] = $symptoms;
                echo json_encode($response);
            }
            else { 
                $response['error'] = true ;
                echo json_encode($response);
            
            }
        }
    }
}
?>