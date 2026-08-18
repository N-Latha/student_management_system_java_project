/**
 * ==========================================================
 * Student Management System - Vanilla JavaScript
 * Clean, Standard DOM manipulation and Event Handling
 * ==========================================================
 */

// Execute after DOM is fully loaded
document.addEventListener("DOMContentLoaded", function () {
    initFormValidation();
    initDeleteModal();
    initAlertAutoDismiss();
    initClientTableSearch();
});

/**
 * 1. Form Validation for Add and Edit Student forms
 */
function initFormValidation() {
    const studentForm = document.getElementById("studentForm");
    if (!studentForm) return;

    // Form input elements
    const nameInput = document.getElementById("name");
    const emailInput = document.getElementById("email");
    const phoneInput = document.getElementById("phone");
    const departmentInput = document.getElementById("department");
    const yearInput = document.getElementById("year");

    // Real-time input validation on user typing
    if (nameInput) nameInput.addEventListener("input", () => validateName(nameInput));
    if (emailInput) emailInput.addEventListener("input", () => validateEmail(emailInput));
    if (phoneInput) phoneInput.addEventListener("input", () => validatePhone(phoneInput));
    if (departmentInput) departmentInput.addEventListener("change", () => validateDepartment(departmentInput));
    if (yearInput) yearInput.addEventListener("change", () => validateYear(yearInput));

    // Form submit event handler
    studentForm.addEventListener("submit", function (event) {
        const isNameValid = validateName(nameInput);
        const isEmailValid = validateEmail(emailInput);
        const isPhoneValid = validatePhone(phoneInput);
        const isDeptValid = validateDepartment(departmentInput);
        const isYearValid = validateYear(yearInput);

        // If any validation fails, prevent standard form submission
        if (!isNameValid || !isEmailValid || !isPhoneValid || !isDeptValid || !isYearValid) {
            event.preventDefault();
            // Scroll to the first invalid input
            const firstInvalid = document.querySelector(".is-invalid");
            if (firstInvalid) {
                firstInvalid.focus();
            }
        }
    });
}

// Validator helper functions
function validateName(input) {
    if (!input) return true;
    const value = input.value.trim();
    if (value.length === 0) {
        showError(input, "Student name is required.");
        return false;
    } else if (value.length < 2) {
        showError(input, "Name must be at least 2 characters.");
        return false;
    }
    clearError(input);
    return true;
}

function validateEmail(input) {
    if (!input) return true;
    const value = input.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (value.length === 0) {
        showError(input, "Email address is required.");
        return false;
    } else if (!emailRegex.test(value)) {
        showError(input, "Please enter a valid email address (e.g., student@example.com).");
        return false;
    }
    clearError(input);
    return true;
}

function validatePhone(input) {
    if (!input) return true;
    const value = input.value.trim();
    // Validate 10-digit numeric phone
    const phoneRegex = /^[0-9]{10}$/;
    if (value.length === 0) {
        showError(input, "Phone number is required.");
        return false;
    } else if (!phoneRegex.test(value)) {
        showError(input, "Phone number must be exactly 10 digits (0-9).");
        return false;
    }
    clearError(input);
    return true;
}

function validateDepartment(input) {
    if (!input) return true;
    const value = input.value.trim();
    if (value.length === 0) {
        showError(input, "Please select a department.");
        return false;
    }
    clearError(input);
    return true;
}

function validateYear(input) {
    if (!input) return true;
    const value = parseInt(input.value, 10);
    if (isNaN(value) || value < 1 || value > 4) {
        showError(input, "Please select a valid academic year (1 to 4).");
        return false;
    }
    clearError(input);
    return true;
}

// Error state display functions
function showError(inputElement, errorMessage) {
    inputElement.classList.add("is-invalid");
    const errorContainer = document.getElementById(inputElement.id + "Error");
    if (errorContainer) {
        errorContainer.textContent = errorMessage;
        errorContainer.style.display = "block";
    }
}

function clearError(inputElement) {
    inputElement.classList.remove("is-invalid");
    const errorContainer = document.getElementById(inputElement.id + "Error");
    if (errorContainer) {
        errorContainer.textContent = "";
        errorContainer.style.display = "none";
    }
}

/**
 * 2. Delete Confirmation Modal
 */
let targetDeleteUrl = "";

function initDeleteModal() {
    const modalOverlay = document.getElementById("deleteModal");
    const cancelBtn = document.getElementById("cancelDeleteBtn");
    const confirmBtn = document.getElementById("confirmDeleteBtn");

    if (cancelBtn) {
        cancelBtn.addEventListener("click", function () {
            closeDeleteModal();
        });
    }

    if (confirmBtn) {
        confirmBtn.addEventListener("click", function () {
            if (targetDeleteUrl) {
                window.location.href = targetDeleteUrl;
            }
        });
    }

    // Close on overlay background click
    if (modalOverlay) {
        modalOverlay.addEventListener("click", function (e) {
            if (e.target === modalOverlay) {
                closeDeleteModal();
            }
        });
    }
}

/**
 * Triggered when user clicks a Delete button on the students table.
 * @param {string} deleteUrl The URL to call (e.g., 'deleteStudent?id=5')
 * @param {string} studentName The name of the student to display in the modal
 */
function confirmDelete(deleteUrl, studentName) {
    targetDeleteUrl = deleteUrl;
    const modalOverlay = document.getElementById("deleteModal");
    const studentNameSpan = document.getElementById("deleteStudentName");

    if (studentNameSpan) {
        studentNameSpan.textContent = studentName || "this student";
    }

    if (modalOverlay) {
        modalOverlay.classList.add("active");
    } else {
        // Fallback to standard browser confirm if custom modal is absent
        const confirmed = window.confirm("Are you sure you want to delete " + (studentName || "this student") + "?");
        if (confirmed) {
            window.location.href = deleteUrl;
        }
    }
}

function closeDeleteModal() {
    const modalOverlay = document.getElementById("deleteModal");
    if (modalOverlay) {
        modalOverlay.classList.remove("active");
    }
    targetDeleteUrl = "";
}

/**
 * 3. Alert auto-dismissal and manual close
 */
function initAlertAutoDismiss() {
    const alerts = document.querySelectorAll(".alert");
    alerts.forEach(function (alert) {
        // Close button click
        const closeBtn = alert.querySelector(".alert-close");
        if (closeBtn) {
            closeBtn.addEventListener("click", function () {
                alert.style.display = "none";
            });
        }

        // Auto-dismiss alert after 4.5 seconds
        setTimeout(function () {
            alert.style.transition = "opacity 0.5s ease, transform 0.5s ease";
            alert.style.opacity = "0";
            alert.style.transform = "translateY(-10px)";
            setTimeout(function () {
                alert.style.display = "none";
            }, 500);
        }, 4500);
    });
}

/**
 * 4. Client-side Live Table Filter (Instant UX search while typing in table)
 */
function initClientTableSearch() {
    const filterInput = document.getElementById("tableSearchInput");
    const table = document.getElementById("studentsTable");

    if (!filterInput || !table) return;

    filterInput.addEventListener("input", function () {
        const filterValue = filterInput.value.toLowerCase().trim();
        const rows = table.querySelectorAll("tbody tr");

        rows.forEach(function (row) {
            const rowText = row.textContent.toLowerCase();
            if (rowText.includes(filterValue)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });
}
