// const track = document.querySelector('.scroll-track');
// const images = document.querySelectorAll('.scroll-track img');
// const leftArrow = document.querySelector('.scroll-arrow.left');
// const rightArrow = document.querySelector('.scroll-arrow.right');
// const dotsContainer = document.querySelector('.scroll-dots');
//
// let index = 0;
// const total = images.length;
//
// // create dots
// for(let i=0;i<total;i++){
//     const dot = document.createElement('span');
//     if(i===0) dot.classList.add('active');
//     dotsContainer.appendChild(dot);
// }
// const dots = dotsContainer.querySelectorAll('span');
//
// // update carousel
// function updateCarousel(){
//     const width = images[0].offsetWidth + 10; // include margin
//     track.style.transform = `translateX(${-index * width}px)`;
//     dots.forEach(dot => dot.classList.remove('active'));
//     dots[index].classList.add('active');
// }
//
// // arrow clicks
// leftArrow.addEventListener('click', () => {
//     index = index > 0 ? index-1 : total-1;
//     updateCarousel();
//     resetAutoScroll();
// });
// rightArrow.addEventListener('click', () => {
//     index = index < total-1 ? index+1 : 0;
//     updateCarousel();
//     resetAutoScroll();
// });
//
// // dot clicks
// dotsContainer.addEventListener('click', e=>{
//     if(e.target.tagName==='SPAN'){
//         index = [...dotsContainer.children].indexOf(e.target);
//         updateCarousel();
//         resetAutoScroll();
//     }
// });
//
// // auto-scroll
// let autoScroll = setInterval(()=>{
//     index = index < total-1 ? index+1 : 0;
//     updateCarousel();
// }, 3000);
//
// // reset auto-scroll
// function resetAutoScroll(){
//     clearInterval(autoScroll);
//     autoScroll = setInterval(()=>{
//         index = index < total-1 ? index+1 : 0;
//         updateCarousel();
//     }, 3000);
// }
//
// // init
// updateCarousel();


const track = document.querySelector('.scroll-track');
const images = document.querySelectorAll('.scroll-track img');
const leftArrow = document.querySelector('.scroll-arrow.left');
const rightArrow = document.querySelector('.scroll-arrow.right');
const dotsContainer = document.querySelector('.scroll-dots');

const totalImages = 5; // Original number of unique images
let position = 0;
const imageWidth = images[0].offsetWidth + 10; // Include margin
const maxScroll = -(totalImages * imageWidth); // Maximum scroll position for original set

// Create dots for original images
for (let i = 0; i < totalImages; i++) {
    const dot = document.createElement('span');
    if (i === 0) dot.classList.add('active');
    dotsContainer.appendChild(dot);
}
const dots = dotsContainer.querySelectorAll('span');

// Update carousel position
function updateCarousel() {
    track.style.transform = `translateX(${position}px)`;
    const activeIndex = Math.abs(position / imageWidth) % totalImages;
    dots.forEach(dot => dot.classList.remove('active'));
    dots[activeIndex].classList.add('active');
}

// Continuous scroll animation
function startContinuousScroll() {
    let animation = requestAnimationFrame(() => {
        position -= 1; // Move 1px at a time for smooth scrolling
        if (position <= maxScroll) {
            position = 0; // Reset to start when reaching the end of original set
        }
        updateCarousel();
        startContinuousScroll(); // Loop the animation
    });
}

// Arrow clicks
leftArrow.addEventListener('click', () => {
    position += imageWidth;
    if (position > 0) position = maxScroll; // Loop back to the end of duplicated set
    updateCarousel();
    resetAutoScroll();
});

rightArrow.addEventListener('click', () => {
    position -= imageWidth;
    if (position < maxScroll) position = 0; // Loop back to start
    updateCarousel();
    resetAutoScroll();
});

// Dot clicks
dotsContainer.addEventListener('click', e => {
    if (e.target.tagName === 'SPAN') {
        const dotIndex = [...dotsContainer.children].indexOf(e.target);
        position = -(dotIndex * imageWidth);
        updateCarousel();
        resetAutoScroll();
    }
});

// Auto-scroll initialization
let autoScroll;
function startAutoScroll() {
    autoScroll = setInterval(() => {
        position -= 1;
        if (position <= maxScroll) {
            position = 0;
        }
        updateCarousel();
    }, 20); // Adjust speed (lower value = faster scroll)
}

function resetAutoScroll() {
    clearInterval(autoScroll);
    startAutoScroll();
}

// Initialize
updateCarousel();
startAutoScroll();