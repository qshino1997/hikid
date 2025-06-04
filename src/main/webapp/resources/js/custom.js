document.addEventListener('DOMContentLoaded', () => {
    const alertEl = document.getElementById('showAlert');
    if (alertEl) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alertEl);
            bsAlert.close();
        }, 3000);
    }
});
