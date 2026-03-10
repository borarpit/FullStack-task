const students = [
{name:"Arpit", dept:"CSE", date:"2023-02-10"},
{name:"Rahul", dept:"IT", date:"2022-08-15"},
{name:"Sneha", dept:"ECE", date:"2023-01-12"},
{name:"Aman", dept:"CSE", date:"2022-12-01"},
{name:"Priya", dept:"IT", date:"2023-03-05"}
];

let filteredStudents=[...students];

function displayStudents(data){
const table=document.querySelector("#studentTable tbody");
table.innerHTML="";

data.forEach(s=>{
const row=`<tr>
<td>${s.name}</td>
<td>${s.dept}</td>
<td>${s.date}</td>
</tr>`;
table.innerHTML+=row;
});

updateDepartmentCount(data);
}

function sortByName(){
filteredStudents.sort((a,b)=>a.name.localeCompare(b.name));
displayStudents(filteredStudents);
}

function sortByDate(){
filteredStudents.sort((a,b)=>new Date(a.date)-new Date(b.date));
displayStudents(filteredStudents);
}

document.getElementById("departmentFilter").addEventListener("change",function(){
const dept=this.value;

if(dept==="All"){
filteredStudents=[...students];
}else{
filteredStudents=students.filter(s=>s.dept===dept);
}

displayStudents(filteredStudents);
});

function updateDepartmentCount(data){
const counts={};

data.forEach(s=>{
counts[s.dept]=(counts[s.dept]||0)+1;
});

const list=document.getElementById("deptCount");
list.innerHTML="";

for(let dept in counts){
list.innerHTML+=`<li>${dept}: ${counts[dept]}</li>`;
}
}

displayStudents(filteredStudents);