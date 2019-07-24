<?php 
    include_once("./BaseDatos.php");
    header('Content-type: application/json; charset=utf-8');
    $method=$_SERVER['REQUEST_METHOD'];

    $obj = json_decode( file_get_contents('php://input') );   
    $objArr = (array)$obj;
	if (empty($objArr))
    {
		$response["success"] = 422;  //No encontro información 
        $response["message"] = "Error: checar json entrada";
        header($_SERVER['SERVER_PROTOCOL']." 422  Error: faltan parametros de entrada json ");		
    }
    else
    {
	    $noc =  $objArr['nocontrol'];
	    $response = array();
        $result = consultaAlumno($noc);
        if (!empty($result)) {
            $response["success"] = "200";
            $response["message"] = "Alumnos encontrados";

            $response["alumno"] = array();
            foreach ($result as $tupla)
            {
                array_push($response["alumno"], $tupla);
            }
        }
        else{
            $response["success"] = "204";
            $response["message"] = "Alumnos no encontrados";
            header($_SERVER['SERVER_PROTOCOL'] . " 500  Error interno del servidor ");
        }
    }
    echo json_encode($response);
?>