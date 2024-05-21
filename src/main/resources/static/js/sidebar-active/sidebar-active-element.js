document.addEventListener('DOMContentLoaded', function() {
    var currentUrl = window.location.pathname;

    var menuLinks = document.querySelectorAll('.menu-link');

    menuLinks.forEach(function(link) {
        var linkUrl = link.getAttribute('href');

        if (currentUrl.startsWith(linkUrl)) {
            link.closest('.menu-item').classList.add('active');
        }
    });
});