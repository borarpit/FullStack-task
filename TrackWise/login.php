<?php
session_start();
include 'connect.php';

// If already logged in, go to dashboard
if(isset($_SESSION['user_id'])){
    header("Location: index.php");
    exit();
}

if(isset($_POST['login'])){

    $username = $_POST['username'];
    $password = $_POST['password'];

    $query = "SELECT * FROM users WHERE username='$username'";
    $result = mysqli_query($conn, $query);
    $user = mysqli_fetch_assoc($result);

    if($user && password_verify($password, $user['password'])){
        $_SESSION['user_id'] = $user['id'];
        $_SESSION['username'] = $user['username'];
        header("Location: index.php");
        exit();
    } else {
        $error = "Invalid Credentials!";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>TrackWise Login</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div class="login-card">
    <h2>TrackWise Login</h2>

    <?php if(isset($error)) echo "<p style='color:red;'>$error</p>"; ?>

    <form method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit" name="login">Login</button>
    </form>

    <br>
    <a href="register.php">Create New Account</a>
</div>

</body>
</html>