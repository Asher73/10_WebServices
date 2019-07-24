<?php 
    include_once("./BaseDatos.php");
    header('Content-type: application/json; charset=utf-8');
    $method=$_SERVER['REQUEST_METHOD'];
    $response = array();
    $result = consultaAlumnos();
    if (!empty($result)) {
        $response["success"] = "202";
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
        header($_SERVER['SERVER_PROTOCOL'] . "500  Error interno del servidor ");
    }
    echo json_encode($response);
?>