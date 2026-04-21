document.addEventListener('DOMContentLoaded', function() {
    console.log('Job Portal Loaded');

    // Simple search filter for jobs page
    const searchInput = document.getElementById('searchJobs');
    if (searchInput) {
        searchInput.addEventListener('keyup', function() {
            const filter = searchInput.value.toLowerCase();
            const jobCards = document.querySelectorAll('.job-card');
            
            jobCards.forEach(card => {
                const text = card.innerText.toLowerCase();
                if (text.includes(filter)) {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    }

    // Auto-hide alerts after 3 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 3000);
    });
});
