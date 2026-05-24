// Backend API
const API_URL =
    "http://localhost:8080/api/shorten";

const ANALYTICS_URL =
    "http://localhost:8080/api/analytics";

const REGISTER_URL =
    "http://localhost:8080/api/auth/register";

const LOGIN_URL =
    "http://localhost:8080/api/auth/login";

// Run On Page Load
window.onload = () => {

    // Dashboard Page
    if(
        window.location.pathname
            .includes("dashboard.html")
    ){

        checkDashboardAuth();

        loadAnalytics();

        // Auto Refresh Analytics
        // Update Timers Every Second
        setInterval(() => {

            updateExpiryTimers();

        },1000);

        // Refresh Analytics Every 5 Seconds
        setInterval(() => {

            loadAnalytics();

        },5000);
            }
};

// Dashboard Protection
function checkDashboardAuth(){

    const token =
        localStorage.getItem(
            "jwtToken"
        );

    // Redirect if not logged in
    if(!token){

        window.location.href =
            "login.html";
    }
}

// Shorten URL
async function shortenUrl() {

    const originalUrlInput =
        document.getElementById(
            "originalUrl"
        );

    const resultDiv =
        document.getElementById(
            "result"
        );

    const shortLinkAnchor =
        document.getElementById(
            "shortLink"
        );

    const errorMessage =
        document.getElementById(
            "errorMessage"
        );

    const copyBtn =
        document.getElementById(
            "copyBtn"
        );

    const qrSection =
        document.getElementById(
            "qrSection"
        );

    const qrCodeContainer =
        document.getElementById(
            "qrcode"
        );

    const button =
        document.querySelector(
            ".url-card button"
        );

    const originalUrl =
        originalUrlInput.value.trim();

    const customAlias =
        document.getElementById(
            "customAlias"
        ).value.trim();

    const expiryHours =
        document.getElementById(
            "expiryHours"
        ).value;

    // Reset
    resultDiv.style.display =
        "none";

    errorMessage.textContent = "";

    // Validation
    if(!originalUrl){

        errorMessage.textContent =
            "Please enter a URL.";

        return;
    }

    try{

        // Loading
        button.innerText =
            "Shortening...";

        button.disabled = true;

        // API Request
        const response =
            await fetch(
                API_URL,
                {
                    method:"POST",

                    headers:{
                        "Content-Type":
                            "application/json",

                        "Authorization":
                            "Bearer " +
                            localStorage.getItem(
                                "jwtToken"
                            )
                    },

                    body:JSON.stringify({

                        originalUrl:
                            originalUrl,

                        customAlias:
                            customAlias,

                        expiryHours:
                            expiryHours || null
                    })
                }
            );

        // Restore Button
        button.innerText =
            "Shorten URL";

        button.disabled = false;

        // Error
        if(!response.ok){

            errorMessage.textContent =
                "Custom alias already exists";

            return;
        }

        // JSON
        const data =
            await response.json();

        // Short URL
        const shortUrl =
            "http://localhost:8080/r/" +
            data.shortCode;

        // Result
        shortLinkAnchor.href =
            shortUrl;

        shortLinkAnchor.textContent =
            shortUrl;

        resultDiv.style.display =
            "block";

        // QR
        // Hide QR Initially
        qrSection.style.display =
            "none";

        // Clear Previous QR
        qrCodeContainer.innerHTML = "";

        // Generate QR
        new QRCode(
            qrCodeContainer,
            {
                text:shortUrl,
                width:180,
                height:180
            }
        );
        // QR Toggle Button
        const qrToggleBtn =
            document.getElementById(
                "qrToggleBtn"
            );

        qrToggleBtn.innerText =
            "Show QR";

        qrToggleBtn.onclick = () => {

            if(
                qrSection.style.display
                === "none"
            ){

                qrSection.style.display =
                    "block";

                qrToggleBtn.innerText =
                    "Hide QR";

            }else{

                qrSection.style.display =
                    "none";

                qrToggleBtn.innerText =
                    "Show QR";
            }
        };
        // Copy Button
        copyBtn.onclick = () => {

            navigator.clipboard
                .writeText(shortUrl);

            copyBtn.innerText =
                "Copied!";

            setTimeout(() => {

                copyBtn.innerText =
                    "Copy";

            },2000);
        };

        // Refresh Dashboard
        loadAnalytics();

    }catch(error){

        console.error(error);

        errorMessage.textContent =
            "Something went wrong.";

        button.innerText =
            "Shorten URL";

        button.disabled = false;
    }
}

// Load Analytics
async function loadAnalytics(){

    try{

        const response =
            await fetch(
                ANALYTICS_URL,
                {
                    headers:{
                        "Authorization":
                            "Bearer " +
                            localStorage.getItem(
                                "jwtToken"
                            )
                    }
                }
            );

        if(!response.ok){
            return;
        }

        const data =
            await response.json();

        // Total Links
        document.getElementById(
            "totalLinks"
        ).innerText =
            data.length;

        // Total Clicks
        let totalClicks = 0;

        data.forEach(url => {

            totalClicks += url.clicks;
        });

        document.getElementById(
            "totalClicks"
        ).innerText =
            totalClicks;

        // Recent List
        const recentList =
            document.getElementById(
                "recentList"
            );

        recentList.innerHTML = "";

        // Empty
        if(data.length === 0){

            recentList.innerHTML = `
                <p style="
                    color:#888;
                    text-align:center;
                    padding:30px;
                ">
                    No URLs created yet.
                </p>
            `;

            return;
        }

        // Render URLs
        data.reverse().forEach(url => {

            const shortUrl =
                "http://localhost:8080/r/" +
                url.shortCode;

            const recentItem =
                document.createElement(
                    "div"
                );

            recentItem.className =
                "recent-item";

            recentItem.innerHTML = `
                <a href="${shortUrl}"
                   target="_blank">

                   ${shortUrl}

                </a>

                <div style="
                    display:flex;
                    gap:10px;
                ">

                    <div class="click-badge">

                        ${url.clicks} Clicks

                        <br>

                        <small class="expiry-timer"
                                data-expiry="${url.expiryTime}">
                        </small>

                    </div>

                    <button
                        class="delete-btn"
                        onclick="deleteUrl('${url.shortCode}')">

                        Delete

                    </button>

                </div>
            `;

            recentList.appendChild(
                recentItem
            );
        });

    }catch(error){

        console.error(
            "Analytics Error:",
            error
        );
    }
}

// Delete URL
async function deleteUrl(
    shortCode
){

    try{

        await fetch(
            `http://localhost:8080/api/delete/${shortCode}`,
            {
                method:"DELETE",

                headers:{
                    "Authorization":
                        "Bearer " +
                        localStorage.getItem(
                            "jwtToken"
                        )
                }
            }
        );

        loadAnalytics();

    }catch(error){

        console.error(
            "Delete Error:",
            error
        );
    }
}

// Register User
// Register User
async function registerUser(){

    const username =
        document.getElementById(
            "registerUsername"
        ).value;

    const email =
        document.getElementById(
            "registerEmail"
        ).value;

    const password =
        document.getElementById(
            "registerPassword"
        ).value;

    try{

        const response =
            await fetch(
                REGISTER_URL,
                {
                    method:"POST",

                    headers:{
                        "Content-Type":
                            "application/json"
                    },

                    body:JSON.stringify({
                        username,
                        email,
                        password
                    })
                }
            );

        // Backend Message
        const message =
            await response.text();

        // Failed
        if(!response.ok){

            alert(message);

            // Redirect To Login
            if(
                message.includes("exists")
            ){

                window.location.href =
                    "login.html";
            }

            return;
        }

        // Success
        alert(message);

        window.location.href =
            "login.html";

    }catch(error){

        console.error(error);

        alert(
            "Something went wrong"
        );
    }
}
// Login User
async function loginUser(){

    const username =
        document.getElementById(
            "loginUsername"
        ).value;

    const password =
        document.getElementById(
            "loginPassword"
        ).value;

    try{

        const response =
            await fetch(
                LOGIN_URL,
                {
                    method:"POST",

                    headers:{
                        "Content-Type":
                            "application/json"
                    },

                    body:JSON.stringify({
                        username,
                        password
                    })
                }
            );

        if(!response.ok){

            alert(
                "Invalid credentials"
            );

            return;
        }

        const token =
            await response.text();

        // Save JWT
        localStorage.setItem(
            "jwtToken",
            token
        );

        // Redirect
        window.location.href =
            "dashboard.html";

    }catch(error){

        console.error(error);
    }
}

// Logout
function logoutUser(){

    localStorage.removeItem(
        "jwtToken"
    );

    window.location.href =
        "login.html";
}

// Update Countdown Timers
// Update Countdown Timers
function updateExpiryTimers(){

    const timers =
        document.querySelectorAll(
            ".expiry-timer"
        );

    timers.forEach(timer => {

        const expiry =
            timer.dataset.expiry;

        // No Expiry
        if(
            !expiry
            ||
            expiry === "null"
        ){

            timer.innerHTML =
                "No Expiry";

            return;
        }

        // Fix Spring Date Format
        const expiryDate =
            new Date(
                expiry.replace("T"," ")
            );

        const now =
            new Date();

        const diff =
            expiryDate - now;

        // Expired
        if(diff <= 0){

            timer.innerHTML =
                "Expired";

            return;
        }

        // Time Calculation
        const hours =
            Math.floor(
                diff / (1000 * 60 * 60)
            );

        const minutes =
            Math.floor(
                (diff % (1000 * 60 * 60))
                /
                (1000 * 60)
            );

        const seconds =
            Math.floor(
                (diff % (1000 * 60))
                / 1000
            );

        timer.innerHTML =
            `Expires in:
             ${hours}h
             ${minutes}m
             ${seconds}s`;
    });
}