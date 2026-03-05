<?php
session_start();
include 'connect.php';

$user_id = $_SESSION['user_id'];

$name = $_POST['name'];
$amount = $_POST['amount'];
$category = $_POST['category'];
$date = $_POST['date'];

$sql = "INSERT INTO expenses (name, amount, category, date, user_id)
VALUES ('$name', '$amount', '$category', '$date', '$user_id')";

mysqli_query($conn, $sql);

header("Location: index.php");
?>