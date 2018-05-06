<?PHP

require 'mysql_login.php';

$json=array();

                $conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);
                $consulta="select id,description from something";
                $resultado=mysqli_query($conexion,$consulta);

                while($registro=mysqli_fetch_array($resultado)){
                        $json['something'][]=$registro;
                }
                mysqli_close($conexion);
                echo json_encode($json);
?>

