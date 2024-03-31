let recognition;

// Function to start listening
function startListening() {
    if ('SpeechRecognition' in window || 'webkitSpeechRecognition' in window) {
        recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();

        // Define settings
        recognition.continuous = true;
        recognition.interimResults = false;

        // Get references to the output element

        // Event handler when the recognition starts
        recognition.onstart = () => {
            console.log('Listening...');
        };

        // Event handler for when speech is recognized
        recognition.onresult = (event) => {
            const lastResult = event.results[event.results.length - 1];
            const transcript = lastResult[0].transcript;
            // output.innerHTML = transcript;

            try {
                if (transcript.includes("open")) {
                    let content = transcript.split("open")[1];
                    content = content.split("please")[0];

                    findElementByContent(content).click()
                }
            } catch (e) {
                console.log('Error: ' + e);
            }
        };

        // Event handler for errors
        recognition.onerror = (event) => {
            console.log('Error: ' + event.error);
        };

        recognition.start();
    }
}

// Function to stop listening
function stopListening() {
    if (recognition) {
        recognition.stop();
    }
}

function findElementByContent(content) {
    // Use querySelectorAll to select all elements that might contain the content
    // const elements = document.querySelectorAll('a');
    // elements.concat();

    // Convert NodeList to arrays
    const anchorArray = Array.from(document.querySelectorAll('a'));
    const inputArray = Array.from(document.querySelectorAll('input[type="submit"]'));
    const test = Array.from(document.querySelectorAll('button'));

// Concatenate the arrays
    const elements = anchorArray.concat(inputArray).concat(test);
    console.log(elements)

    // Filter elements that match the content
    const matchingElements = Array.from(elements).filter(element => {
        let elementContent = element.textContent.trim();

        if (!elementContent) {
            elementContent = element.value;

            if (elementContent) {
                elementContent = elementContent.trim();
            } else {
                elementContent = '';
            }
        }

        console.log(elementContent.toLowerCase())
        console.log(content.trim().toLowerCase())
        return elementContent.toLowerCase().includes(content.trim().toLowerCase());

    });

    // Return the most suitable element, e.g., the first matching element
    if (matchingElements.length > 0) {
        return matchingElements[0];
    } else {
        return null; // If no matching elements were found
    }
}

// // Start listening when the page loads
// window.onload = startListening;
//
// // Stop listening when the page unloads or when the user navigates away
// window.onunload = stopListening;
// window.onbeforeunload = stopListening;
document.addEventListener('DOMContentLoaded', startListening);

window.addEventListener("focus", startListening);
window.addEventListener("blur", stopListening);
