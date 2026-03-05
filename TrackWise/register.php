<?php
include 'connect.php';

if(isset($_POST['register'])){

$username = $_POST['username'];
$password = password_hash($_POST['password'], PASSWORD_DEFAULT);

$query = "INSERT INTO users(username,password)
VALUES('$username','$password')";

if(mysqli_query($conn,$query)){
header("Location: login.php");
}else{
$error = "Username already exists!";
}
}
?>

<!DOCTYPE html>
<html>
<head>
<title>Register</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<div class="login-card">
<h2>Create Account</h2>

<?php if(isset($error)) echo "<p style='color:red'>$error</p>"; ?>

<form method="post">
<input type="text" name="username" placeholder="Username" required>
<input type="password" name="password" placeholder="Password" required>
<button name="register">Register</button>
</form>

<a href="login.php">Already have account? Login</a>
</div>

</body>
</html>