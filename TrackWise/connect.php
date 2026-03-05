<?php
$host = "127.0.0.1";
$user = "root";
$password = "";
$database = "trackwise";
$port = 3307;

$conn = mysqli_connect($host, $user, $password, $database, $port);

if (!$conn) {
    die("Database Connection Failed: " . mysqli_connect_error());
}
?>