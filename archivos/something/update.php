<?PHP

require 'mysql_login.php'; 

	$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);
	$id = $_POST["id"];
	$description = $_POST["description"];

	$sql="UPDATE something SET description= ? WHERE id=?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('si',$description,$id);
		
	if($stm->execute()){
		echo "actualiza";
	}else{
		echo "noActualiza";
	}
	mysqli_close($conexion);
?>

