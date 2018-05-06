<?PHP

require 'mysql_login.php';

$json=array();

	if(isset($_GET["id"])){
		$id=$_GET["id"];
		$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);

		$consulta="select id,description from something where id='{$id}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$json['something'][]=$registro; //poner nombre de la bd entre los corchetes, para este caso "something"
		}else{
			$resultar["id"]=0;
			$resultar["description"]='no registrado';
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['usuario'][]=$resultar;
		echo json_encode($json);
	}
?>
