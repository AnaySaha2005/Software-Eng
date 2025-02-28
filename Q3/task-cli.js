const { Command } = require('commander');
const fs = require('fs');
const path = require('path');

const program = new Command();
const TASKS_FILE = path.join(__dirname, 'tasks.json');

// Load tasks
const loadTasks = () => {
  if (!fs.existsSync(TASKS_FILE)) return [];
  const data = fs.readFileSync(TASKS_FILE);
  return JSON.parse(data);
};

// Save tasks
const saveTasks = (tasks) => {
  fs.writeFileSync(TASKS_FILE, JSON.stringify(tasks, null, 2));
};

// Add Task
program.command('add <task>').description('Add a new task').action((task) => {
  const tasks = loadTasks();
  tasks.push({ id: tasks.length + 1, task, completed: false });
  saveTasks(tasks);
  console.log(`Task added: ${task}`);
});

// List Tasks
program.command('list').description('List all tasks').action(() => {
  const tasks = loadTasks();
  if (tasks.length === 0) return console.log('No tasks found.');
  tasks.forEach(({ id, task, completed }) => {
    console.log(`${id}. ${task} [${completed ? '✓' : '✗'}]`);
  });
});

// Complete Task
program.command('complete <id>').description('Mark a task as complete').action((id) => {
  const tasks = loadTasks();
  const task = tasks.find((t) => t.id === parseInt(id));
  if (!task) return console.log('Task not found.');
  task.completed = true;
  saveTasks(tasks);
  console.log(`Task ${id} marked as complete.`);
});

// Edit Task
program.command('edit <id> <newTask>').description('Edit an existing task').action((id, newTask) => {
  const tasks = loadTasks();
  const task = tasks.find((t) => t.id === parseInt(id));
  if (!task) return console.log('Task not found.');
  task.task = newTask;
  saveTasks(tasks);
  console.log(`Task ${id} updated.`);
});

// Run CLI
program.parse(process.argv);
