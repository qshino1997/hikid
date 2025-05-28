document.addEventListener('DOMContentLoaded', () => {
    const toastErEl = document.getElementById('errorToast');
    if (toastErEl) {
        const bsErToast = new bootstrap.Toast(toastErEl, {
            delay: 3000
        });
        bsErToast.show();
    }

    const toastEl = document.getElementById('logoutToast');
    if (toastEl) {
        const bsToast = new bootstrap.Toast(toastEl, {
            delay: 3000
        });
        bsToast.show();
    }

    const alertEl = document.getElementById('showAlert');
    if (alertEl) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alertEl);
            bsAlert.close();
        }, 3000);
    }
});
