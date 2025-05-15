// Chuẩn bị instance toast sẵn
const cartToastEl = document.getElementById('cartToast');
const cartToast = new bootstrap.Toast(cartToastEl, { delay: 2000 });

document.querySelectorAll('.js-add-to-cart').forEach(btn => {
    btn.addEventListener('click', function() {
        const pid = this.dataset.productId;  // data-product-id → dataset.productId
        const form = new URLSearchParams({ product_id: pid, quantity: 1 });

        fetch(window.ADD_TO_CART_URL, {
            method: 'POST',
            headers: {
                'Accept': 'application/json'
            },
            body: form
        })
            .then(res => {
                if (!res.ok) throw new Error('HTTP ' + res.status);
                return res.json();
            })
            .then(data => {
                // Cập nhật badge
                document.getElementById('cart-count').textContent = data.totalQuantity;
                // Update message
                cartToastEl.querySelector('.toast-body')
                    .textContent = `Đã thêm "${this.dataset.productName}" vào giỏ!`;
                // Show toast thành công
                cartToast.show();
            })
            .catch(err => console.error('Add to cart failed:', err));
    });
});
