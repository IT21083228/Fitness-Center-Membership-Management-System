setTimeout(() => {
    const toast = document.querySelector(".toast");
    if (toast) {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(-20px)";

        setTimeout(() => {
            toast.remove();
        }, 500);
    }
}, 3000);