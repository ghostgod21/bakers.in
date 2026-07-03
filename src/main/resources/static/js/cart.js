// Bakers.in — light front-end touches. The cart itself is fully server-rendered
// (session-based, via Spring MVC), so this file only adds small UX niceties.

document.addEventListener('DOMContentLoaded', () => {
    // Briefly confirm "Add to cart" was submitted, for pages with many forms.
    document.querySelectorAll('.add-to-cart-form').forEach((form) => {
        form.addEventListener('submit', () => {
            const btn = form.querySelector('.btn-add');
            if (btn) {
                btn.textContent = 'Added ✓';
                btn.disabled = true;
            }
        });
    });
});
