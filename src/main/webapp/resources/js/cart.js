// Chuẩn bị instance toast sẵn
const cartToastEl = document.getElementById('cartToast');
const cartToast = new bootstrap.Toast(cartToastEl, { delay: 2000 });

document.addEventListener('click', function (e) {
    const btn = e.target.closest('.js-add-to-cart');
    if (!btn) return;

    const pid = btn.dataset.productId;
    const qty = btn.dataset.quantity || 1;
    const mode = btn.dataset.mode || 2;  // mode mặc định là 2
    const productName = btn.dataset.productName || 'Sản phẩm';

    const form = new URLSearchParams({ productId: pid, quantity: qty, mode });

    fetch(window.ADD_TO_CART_URL, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: form
    })
        .then(res => {
            if (!res.ok) throw new Error('HTTP ' + res.status);
            return res.json();
        })
        .then(data => {
            // Cập nhật số lượng trên badge
            const cartCountEl = document.getElementById('cart-count');
            if (cartCountEl) cartCountEl.textContent = data.totalQuantity;

            // Cập nhật và hiển thị toast
            const cartToastEl = document.getElementById('cartToast');
            if (cartToastEl) {
                cartToastEl.querySelector('.toast-body').textContent = `Đã thêm "${productName}" vào giỏ!`;
                const toast = bootstrap.Toast.getOrCreateInstance(cartToastEl);
                toast.show();
            }
        })
        .catch(err => console.error('Add to cart failed:', err));
});

