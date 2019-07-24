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
	    $response = array();
        $result = insertaAlumno($objArr);
        if ($result) {
            $response["success"] = "201";
            $response["message"] = "Se Agrego Alumno";
        }
        else{
            $response["success"] = "409";
            $response["message"] = "Alumnos no se Agrego";
            header($_SERVER['SERVER_PROTOCOL'] . " 409  Conflicto al Insertar ");
        }
    }
    echo json_encode($response);
?>