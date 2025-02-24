

console.log("This is Script File")


const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {

		$(".sidebar").css("display", "none")
		$(".content").css("margin-left", "0%")
	} else {
		$(".sidebar").css("display", "block")
		$(".content").css("margin-left", "20%")
	}
};


  function storeSelectedOption(questionId, selectedOption) {
      let selectedOptions = JSON.parse(sessionStorage.getItem("selectedOptions")) || {};
      selectedOptions[questionId] = selectedOption;
      sessionStorage.setItem("selectedOptions", JSON.stringify(selectedOptions));
    }

   // Attach event listeners to the radio buttons
   let radioButtons = document.querySelectorAll("input[type='radio']");
   radioButtons.forEach(function(radioButton) {
     radioButton.addEventListener("click", function() {
       let questionId = this.name.replace("question-", "");
       let selectedOption = this.value;
       storeSelectedOption(questionId, selectedOption);
       updateCheckboxes();
     });
   });


    // Attach event listeners to the option labels
    let optionLabels = document.querySelectorAll(".option label");
    console.log(optionLabels);
    optionLabels.forEach(function(label) {
      label.addEventListener("click", function() {
        let radioButton = document.querySelector("#" + this.getAttribute("for"));
        console.log("Label clicked");
        radioButton.click();
      });
    });

    // Submit the form with the stored selected options when the last page is reached
    if (document.querySelector(".submit")) {
     try{
      console.log("submit function is being called")
      let selectedOptions = JSON.parse(sessionStorage.getItem("selectedOptions")) || {};

      for (let questionId in selectedOptions) {
        let input = document.createElement("input");
        input.type = "hidden";
        input.name = "question-" + questionId;
        input.value = selectedOptions[questionId];
        document.getElementById("question-form").appendChild(input);
      }

      for (let questionId in selectedOptions) {
        let input = document.createElement("input");
        input.type = "hidden";
        input.name = "questionIds[]";
        input.value = questionId;
        document.getElementById("question-form").appendChild(input);
      }
      } catch(e){
      console.log("error while submitting data")
      console.log(e)
      }
    }

    // Function to set the options based on the stored values in the session
    function setSelectedOptions() {
      let selectedOptions = JSON.parse(sessionStorage.getItem("selectedOptions")) || {};

      for (let questionId in selectedOptions) {
        let selectedOption = selectedOptions[questionId];
        let optionElement = document.querySelector("input[name='question-" + questionId + "'][value='" + selectedOption + "']");
        if (optionElement) {
          optionElement.checked = true;
        }
      }
    }

    setSelectedOptions();







const examDuration = 20; // duration of the exam in minutes
// Check if the end time is stored in session storage
let endTime = sessionStorage.getItem('endTime');



 function startTimer() {
    // Your function logic goes here
    console.log("Start Assessment clicked");

      // Calculate the end time of the exam
      const now = new Date();
      endTime = new Date(now.getTime() + examDuration * 60000);

      // Store the end time in session storage
      sessionStorage.setItem('endTime', endTime);

  }

  // Add click event listener to the anchor tag
  if(document.getElementById("user-link-testing")){
  document.getElementById("user-link-testing").addEventListener("click", startTimer);
  }
 if (endTime) {
      // Parse the end time from session storage
      endTime = new Date(endTime);
    }
// Update the timer every second
const timer = setInterval(() => {
  // Calculate the remaining time
  const now = new Date();
  const remainingTime = endTime - now;

 // Check if the time is up
 if (remainingTime <= 0) {
   clearInterval(timer);
   document.getElementById("timer").textContent = "Time's up!";

   // Automatically submit the exam
   console.log("before submit the exam");
   document.getElementById("question-form").submit();
    console.log("after submit ");
  } else {
    // Update the timer display
    const minutes = Math.floor(remainingTime / 60000);
    const seconds = Math.floor((remainingTime % 60000) / 1000);
    document.getElementById("timer").textContent = `${minutes}:${seconds.toString().padStart(2, "0")}`;
  }
}, 1000);



// Retrieve the user ID from the HTML page
const userIdElement = document.querySelector('.selected-info h5 span');
const userId = userIdElement.textContent;

// Make the AJAX call to the server to retrieve the question IDs
fetch(`/user/get-question-ids?userId=${userId}`)
  .then(response => response.json())
  .then(data => {
    const idlist = data;
    const questionList = document.getElementById('question-list');

    // Get the selected options from session storage
    let selectedOptions = JSON.parse(sessionStorage.getItem("selectedOptions")) || {};

    for (let i = 0; i < idlist.length; i++) {
      const li = document.createElement('li');
      li.setAttribute('data-question-id', idlist[i]);

      // create a span element to hold the question number
      const span = document.createElement('span');
      span.textContent = 'Q. ' + (i + 1);
      li.appendChild(span);

      // add a non-breaking space after the question number
      li.innerHTML += ' ';

      // create a checkbox element
      const checkbox = document.createElement('input');
      checkbox.type = 'checkbox';

      // check if the question has been answered
      if (selectedOptions.hasOwnProperty(idlist[i])) {
        console.log('checkbox selected');
        checkbox.checked = true;
      }

      // attach a click event listener to the checkbox
      checkbox.addEventListener('click', function() {
        let questionId = this.parentNode.getAttribute('data-question-id');
        let selectedOption = this.checked ? this.value : null;
        storeSelectedOption(questionId, selectedOption);
      });

      // append the checkbox to the list item
      li.appendChild(checkbox);

      // If the option is selected, set the checkbox as checked
      if (selectedOptions.hasOwnProperty(idlist[i])) {
        checkbox.checked = true;
      }

      // append the list item to the question list
      questionList.appendChild(li);
    }
  });
function updateCheckboxes() {
  // Get the selected options from session storage
  let selectedOptions = JSON.parse(sessionStorage.getItem("selectedOptions")) || {};

  // Get all the checkboxes
  let checkboxes = document.querySelectorAll("#question-list input[type='checkbox']");

  // Update the state of each checkbox
  checkboxes.forEach(function(checkbox) {
    let questionId = checkbox.parentNode.getAttribute('data-question-id');
    checkbox.checked = selectedOptions.hasOwnProperty(questionId);
  });
}

  function clearSessionStorage() {
    sessionStorage.clear();
  }
