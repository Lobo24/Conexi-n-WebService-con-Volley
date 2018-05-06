<?PHP

require 'mysql_login.php';


	if(isset($_GET["id"])){
		$id=$_GET["id"];

		$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);
		$sql="DELETE FROM something WHERE id= ? ";
		$stm=$conexion->prepare($sql);
		$stm->bind_param('i',$id);

		if($stm->execute()){
			echo "elimina";
		}else{
			echo "noElimina";
		}

		mysqli_close($conexion);
	}
	else{
		echo "noExiste";
	}
?>
