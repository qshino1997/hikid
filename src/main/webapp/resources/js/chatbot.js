document.addEventListener("DOMContentLoaded", () => {
    const popup = document.getElementById("mychatbot-popup");
    const openBtn = document.getElementById("open-mychatbot");
    const closeBtn = document.getElementById("mychatbot-close");
    const sendBtn = document.getElementById("mychat-send");
    const input = document.getElementById("mychat-input");
    const body = document.getElementById("mychatbot-body");

    // Mở popup
    openBtn.addEventListener("click", () => {
        popup.classList.add("open");
        input.focus();
    });

    // Đóng popup
    closeBtn.addEventListener("click", () => {
        popup.classList.remove("open");
    });

    // Gửi và hiển thị tin nhắn
    sendBtn.addEventListener("click", () => {
        const text = input.value.trim();
        if (!text) return;
        appendMessage("user", text);
        input.value = "";
        sendMessageToDialogflow(text);
    });

    // Nhấn Enter cũng gửi
    input.addEventListener("keypress", e => {
        if (e.key === "Enter") sendBtn.click();
    });

    // Hàm thêm message vào body
    function appendMessage(who, text) {
        const msg = document.createElement("div");
        msg.classList.add("mychat-message", who);
        const bubble = document.createElement("div");
        bubble.classList.add("mychat-bubble");
        bubble.textContent = text;
        msg.appendChild(bubble);
        body.appendChild(msg);
        body.scrollTop = body.scrollHeight;
    }

    // Gửi câu hỏi đến Dialogflow
    function sendMessageToDialogflow(userQuery) {
        fetch("/chatbot/ask", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ userQuery: userQuery })
        })
            .then(res => res.json())
            .then(data => {
                if (data.qty != null) {
                    const cartCountEl = document.getElementById('cart-count');
                    if (cartCountEl) cartCountEl.textContent = data.qty;
                }
                appendMessage("bot", data.response);
            })
            .catch(() => appendMessage("bot", "Lỗi kết nối, xin thử lại sau"));
    }
});