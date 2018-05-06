<?PHP

require 'mysql_login.php';

$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);

	$description = $_POST["description"]; //se debe declarar cada variable acorde a las de la base de datos
	$sql="INSERT INTO something(description) VALUES (?)";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('s',$description); //El primer parámetro de  bind_param corresponde al orden de datatypes, 
				       //en este caso es 's' de string, de ser un string, un integer y un double, sería 'sid',
				       //despues se ponen los atributos correspondientes
	if($stm->execute()){
		echo "Registrado";
	}else{
		echo "No se pudo";
	}
	
	mysqli_close($conexion);
?>
