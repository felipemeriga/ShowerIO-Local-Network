var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var minify = require('gulp-minify-css');

gulp.task('css', function(){
   gulp.src('build/*.css')
   .pipe(concat('../build/skeleton.min.css'))
   .pipe(minify())
   .pipe(gulp.dest('css/'));
});

gulp.task('default',['css'],function(){
});